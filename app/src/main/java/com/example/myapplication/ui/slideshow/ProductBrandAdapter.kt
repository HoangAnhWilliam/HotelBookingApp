package com.example.myapplication.ui.slideshow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class ProductBrandAdapter : RecyclerView.Adapter<ProductBrandAdapter.ViewHolder>() {

    private var items: List<Product> = listOf()

    fun setProducts(newProducts: List<Product>) {
        items = newProducts
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.text_name)
        val brandTextView: TextView = itemView.findViewById(R.id.text_brand)
        val categoryTextView: TextView = itemView.findViewById(R.id.text_category)
        val priceTextView: TextView = itemView.findViewById(R.id.text_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = items[position]
        holder.nameTextView.text = product.name
        holder.brandTextView.text = "Brand: ${product.brand.name}"
        holder.categoryTextView.text = "Category: ${product.category.name}"
        holder.priceTextView.text = "Price: ${product.price}"
    }

    override fun getItemCount() = items.size

    // Phương thức để cập nhật danh sách sản phẩm
    fun setItems(newItems: List<Product>) {
        items = newItems
        notifyDataSetChanged()  // Cập nhật giao diện khi dữ liệu thay đổi
    }
}
