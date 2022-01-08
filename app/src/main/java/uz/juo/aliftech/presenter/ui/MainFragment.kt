package uz.juo.aliftech.presenter.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import uz.juo.aliftech.R
import uz.juo.aliftech.adapter.BookRvAdapter
import uz.juo.aliftech.databinding.FragmentMainBinding
import uz.juo.aliftech.utils.BookResource
import uz.juo.aliftech.viewmodel.BookViewModel
import uz.juo.domain.models.Data

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewmodel: BookViewModel by viewModels()
    lateinit var adapter: BookRvAdapter
    var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val progressBar = binding.progress as ProgressBar
        val doubleBounce: Sprite = FadingCircle()
        progressBar.indeterminateDrawable = doubleBounce
        loadData()
        return binding.root
    }

    @InternalCoroutinesApi
    private fun loadData() {
        lifecycleScope.launch {
            viewmodel.getBooks().collect(object : FlowCollector<BookResource> {
                override suspend fun emit(value: BookResource) {
                    when (value) {
                        is BookResource.Success -> {
                            binding.progress.visibility = View.INVISIBLE
                            adapter = BookRvAdapter(value.data?.data as ArrayList<Data>,
                                object : BookRvAdapter.itemOnClick {
                                    override fun itemClickListener(data: Data, position: Int) {
                                        findNavController().navigate(
                                            R.id.infoFragment,
                                            bundleOf(Pair("param1", data.url))
                                        )
                                    }
                                })
                            binding.rv.adapter = adapter
                            Log.d(TAG, "onCreate:${value.data} ")
                        }
                        is BookResource.Error -> {
                            Snackbar.make(requireView(), value.toString(), Snackbar.LENGTH_SHORT)
                                .show()
                            Log.d(TAG, "onCreateError:$value ")
                        }
                        else -> {
                            Log.d(TAG, "emit: Error")
                        }
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}