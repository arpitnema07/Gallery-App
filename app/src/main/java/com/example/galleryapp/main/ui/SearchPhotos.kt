package com.example.galleryapp.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galleryapp.databinding.FragmentSearchBinding
import com.example.galleryapp.main.MainViewModel
import com.example.galleryapp.main.adapter.SearchAdapter
import com.example.galleryapp.models.Result
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit


class SearchPhotos : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var binding: FragmentSearchBinding


    override fun onResume() {
        super.onResume()
        viewModel.hideSearch.value = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false)

        searchAdapter = SearchAdapter()
        binding.photos.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = searchAdapter
        }

        fromView()
            .debounce(500, TimeUnit.MILLISECONDS)
            .filter { text -> text.isNotEmpty() && text.length >= 3 }
            .map { text -> text.lowercase(Locale.ROOT).trim() }
            .distinctUntilChanged()
            .switchMap { s -> Observable.just(s) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { query ->
                lifecycleScope.launch {
                    viewModel.searchPhotos(query)
                        .collect { result ->
                            when (result) {
                                is Result.Success -> {
                                    searchAdapter.submitList(result.data)
                                    binding.progressBar.visibility = View.GONE
//                                    errorTextView.visibility = View.GONE
                                }
                                is Result.Error -> {
                                    binding.progressBar.visibility = View.GONE
//                                    errorTextView.text = result.message
//                                    errorTextView.visibility = View.VISIBLE
                                }
                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
//                                    errorTextView.visibility = View.GONE
                                }
                            }
                        }
                }
            }



        return binding.root
    }


    private fun fromView(): Observable<String> {
        val subject = PublishSubject.create<String>()
        binding.searchBar.doOnTextChanged { text, _, _, _ ->
            subject.onNext(text.toString())
        }
        return subject
    }

}