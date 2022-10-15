package com.judylee.kurlytest.presentation.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<VH : BaseViewHolder, T : Any> :
    RecyclerView.Adapter<VH>() {

    protected val items: MutableList<T> = mutableListOf()
    protected var vm : BaseViewModel? = null

    fun setItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
    }

    fun setVM(vm : BaseViewModel){
        this.vm = vm
    }

    fun addItems(items: List<T>) {
        this.items.addAll(items)
    }

    fun addItem(item: T) {
        this.items.add(item)
    }

    /**
     * DiffUtil.Callback을 통해서 두 리스트 간에 다른 아이템만을 파악하여 교체함으로써 리스트 업데이트에 성능을 향상한다.
     * 아래 함수는 DiffUtil.Callback 을 구현하는 함수이다.
     */
    open fun setItemAndDiffUtil(newItems: List<T>) {
        //자식 클래스에서 구체화 한다.
    }
}