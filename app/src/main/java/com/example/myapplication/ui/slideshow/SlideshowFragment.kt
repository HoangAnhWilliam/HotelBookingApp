package com.example.myapplication.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSlideshowBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private lateinit var adapter: ProductBrandAdapter // Adapter để hiển thị dữ liệu
    //private var setItems: List<Product> = listOf()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textSlideshow
        //slideshowViewModel.text.observe(viewLifecycleOwner) {
        //    textView.text = "User ID: $userId\nUser Name: $userName"
        //}

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductBrandAdapter()
        binding.recyclerView.adapter = adapter

        // Set click listeners for buttons
        binding.btnGetProducts.setOnClickListener { fetchProducts() }
        binding.btnGetBrands.setOnClickListener { fetchBrands() }
        binding.btnClear.setOnClickListener { clearList() }

        return root
    }
    private fun fetchProducts() {
        RetrofitClient.instance.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val products = response.body()
                    products?.let { adapter.setProducts(it) }
                } else {
                    Toast.makeText(context, "Failed to fetch products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchBrands() {
        RetrofitClient.instance.getBrands().enqueue(object : Callback<List<Brand>> {
            override fun onResponse(call: Call<List<Brand>>, response: Response<List<Brand>>) {
                if (response.isSuccessful) {
                    val brands = response.body()
                    val brandNames = brands?.map { it.name } ?: listOf()
                    adapter.setProducts(brandNames.map { Product(0, it, "", "", 0.0, 0, 0, Category(0, "", "", "", 0), Brand(0, it, "", "", 0), "") })
                } else {
                    Toast.makeText(context, "Failed to fetch brands", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Brand>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearList() {
        adapter.setItems(listOf())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}