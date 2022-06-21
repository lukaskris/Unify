package id.co.app.core.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy


/**
 * Created by Lukas Kristianto on 09/04/22 19.24.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
abstract class BaseActivity<VM : ViewModel, D : ViewDataBinding>(
    @LayoutRes private val layout: Int,
    private val viewModelClass: Class<VM>
) : AppCompatActivity() {
    lateinit var binding: D
    protected lateinit var viewModel: VM private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent()
        initView()
    }

    protected fun setContent() {
        binding = DataBindingUtil.setContentView(this, layout)
        viewModel = ViewModelLazy(
            viewModelClass.kotlin,
            { viewModelStore },
            { defaultViewModelProviderFactory }).value
    }

    abstract fun initView()
}