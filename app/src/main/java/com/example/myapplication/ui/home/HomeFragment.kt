package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Tìm WebView từ layout
        webView = root.findViewById(R.id.homeWeb)

        // Cấu hình WebView
        webView.webViewClient = WebViewClient() // Để mở trang trong app thay vì trình duyệt
        webView.settings.javaScriptEnabled = true // Cho phép JavaScript nếu cần
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT

        // Tải URL
        webView.loadUrl("http://adobahotel.somee.com/") // Thay bằng URL của bạn

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}