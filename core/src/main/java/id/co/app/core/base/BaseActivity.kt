package id.co.app.core.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType


/**
 * Created by Lukas Kristianto on 09/04/22 19.24.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
abstract class BaseActivity<VM : ViewModel, D : ViewDataBinding>(
    @LayoutRes private val layout: Int
) : AppCompatActivity() {
    lateinit var binding: D
    protected lateinit var viewModel: VM private set

    private val type = (javaClass.genericSuperclass as ParameterizedType)
    private val classVM = type.actualTypeArguments[0] as Class<VM>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent()
        initView()
    }

    protected fun setContent(){
        binding = DataBindingUtil.setContentView(this, layout)
        viewModel = ViewModelLazy(classVM.kotlin, { viewModelStore }, { defaultViewModelProviderFactory }).value
    }

    abstract fun initView()
}