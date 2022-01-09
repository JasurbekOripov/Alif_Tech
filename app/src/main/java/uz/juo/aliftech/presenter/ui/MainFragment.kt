package uz.juo.aliftech.presenter.ui

import android.app.Dialog
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
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
import uz.juo.data.room.AppDataBase
import uz.juo.data.room.entity.BookEntity
import uz.juo.domain.models.Data
import android.widget.AbsListView
import android.widget.TextView
import uz.juo.aliftech.utils.NetworkHelper


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewmodel: BookViewModel by viewModels()
    var liveData = ArrayList<Data>()
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

    @RequiresApi(Build.VERSION_CODES.M)
    @OptIn(InternalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        adapter = BookRvAdapter(
            object : BookRvAdapter.itemOnClick {
                override fun itemClickListener(data: Data, position: Int) {
                    findNavController().navigate(
                        R.id.infoFragment,
                        bundleOf(Pair("param1", data.url))
                    )
                }
            },requireContext())
        binding.rv.adapter = adapter
        val progressBar = binding.progress as ProgressBar
        val doubleBounce: Sprite = FadingCircle()
        progressBar.indeterminateDrawable = doubleBounce
        binding.progress.visibility = View.VISIBLE
        loadBooks()
        binding.swiper.setOnRefreshListener {
            loadBooks()
        }
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                loadData()
            }
        })
        return binding.root
    }

    @InternalCoroutinesApi
    private fun loadBooks() {
        binding.swiper.isRefreshing = false
        if (NetworkHelper(requireContext()).isNetworkConnected()) {
            lifecycleScope.launch {
                viewmodel.getBooks().collect(object : FlowCollector<BookResource> {
                    override suspend fun emit(value: BookResource) {

                        when (value) {
                            is BookResource.Success -> {
                                liveData = ArrayList()
                                liveData.addAll(value.data?.data as ArrayList<Data>)
                                Log.d(TAG, "onCreate:${value.data} ")
                                binding.progress.visibility = View.INVISIBLE
                                binding.rv.visibility = View.VISIBLE
                                loadData()
                            }
                            is BookResource.Error -> {
                                Snackbar.make(
                                    requireView(),
                                    value.toString(),
                                    Snackbar.LENGTH_SHORT
                                )
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
        } else {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog)
            dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogBtn_okay = dialog.findViewById(R.id.grant) as TextView
            dialogBtn_okay.setOnClickListener {
                loadBooks()
                dialog.cancel()
            }
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    private fun loadData() {
        var newList = ArrayList<Data>()
        if (!liveData.isNullOrEmpty()) {
            if (adapter.currentList.size + 3 < liveData.size) {
                for (i in 0 until adapter.currentList.size + 3) {
                    var data = liveData[i]
                    newList.add(data)
                    viewmodel.dao.addBook(
                        BookEntity(
                            endDate = data.endDate,
                            icon = data.icon,
                            name = data.name,
                            startDate = data.startDate,
                            url = data.url
                        )
                    )
                }

            } else {
                newList = liveData
            }
            lifecycleScope.launch {
                adapter.submitList(newList)
            }
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