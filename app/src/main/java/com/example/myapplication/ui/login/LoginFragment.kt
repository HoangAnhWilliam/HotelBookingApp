package com.example.myapplication.ui.login

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.databinding.FragmentLoginBinding

import com.example.myapplication.R
import com.example.myapplication.ui.slideshow.SlideshowFragment
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        //loginViewModel.loginResult.observe(viewLifecycleOwner,
        //    Observer { loginResult ->
        //        loginResult ?: return@Observer
        //        loadingProgressBar.visibility = View.GONE
        //        loginResult.error?.let {
        //            showLoginFailed(it)
        //        }
        //        loginResult.success?.let {
        //            updateUiWithUser(it)
        //        }
        //    })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            //loginViewModel.login(
            //    usernameEditText.text.toString(),
            //    passwordEditText.text.toString()
            //)
            performLogin(usernameEditText.text.toString(), passwordEditText.text.toString())
        }
    }

    //private fun updateUiWithUser(model: LoggedInUserView) {
    //    val welcome = getString(R.string.welcome) + model.displayName
    //    // TODO : initiate successful logged in experience
    //    val appContext = context?.applicationContext ?: return
    //Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    //}

    //private fun showLoginFailed(@StringRes errorString: Int) {
    //    val appContext = context?.applicationContext ?: return
    //    Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    //}

    private fun performLogin(email: String, phoneNumber: String) {
        val apiService = RetrofitClient.instance
        val loginRequest = LoginRequest(email, phoneNumber)

        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                binding.loading.visibility = View.GONE
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        // Lưu thông tin người dùng vào SharedPreferences
                        val sharedPref =
                            activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                        sharedPref?.edit()?.clear()?.apply {
                            putString("userId", it.userId)
                            putString("userName", it.userName)
                            apply()
                        }
                        Toast.makeText(
                            context,
                            "Đăng nhập thành công: ${user?.userName}",
                            Toast.LENGTH_LONG
                        ).show()
                        // TODO: Chuyển đến màn hình tiếp theo hoặc lưu thông tin người dùng
                        updateNavHeader()
                    }
                } else {
                    Toast.makeText(context, "Đăng nhập thất bại! Kiểm tra lại thông tin.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                binding.loading.visibility = View.GONE
                Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateNavHeader() {
        // Lấy NavigationView và HeaderView
        val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)
        val headerView = navView.getHeaderView(0) // Lấy Header đầu tiên trong NavigationView

        // Lấy dữ liệu từ SharedPreferences
        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPref?.getString("userName", "Unknown User")
        val userId = sharedPref?.getString("userId", "Unknown ID")

        // Tìm các TextView trong Header và cập nhật giá trị
        val userNameTextView = headerView.findViewById<TextView>(R.id.usernameTextView)
        val userIdTextView = headerView.findViewById<TextView>(R.id.useridTextView)

        userNameTextView?.text = userName
        userIdTextView?.text = userId
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}