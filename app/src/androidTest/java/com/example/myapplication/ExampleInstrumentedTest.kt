package com.example.myapplication

import com.example.myapplication.model.GroceryItem
import com.example.myapplication.viewmodel.GroceryViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GroceryViewModelTest {


    private lateinit var viewModel: GroceryViewModel

    @Before
    fun setUp() {
        viewModel = GroceryViewModel(FakeGroceryDao())
    }

    @Test
    fun adding_an_item_updates_the_list_correctly() {
        val item = GroceryItem(name = "Apples", quantity = 3)
        viewModel.addItem(item)

        val items = viewModel.items.value ?: emptyList()
        assertEquals(1, items.size)
        assertEquals(item, items[0])
    }

    @Test
    fun deleting_an_item_removes_it_from_the_list() {
        val item = GroceryItem(name = "Milk", quantity = 1)
        viewModel.addItem(item)
        viewModel.deleteItem(item)

        val items = viewModel.items.value ?: emptyList()
        assertEquals(0, items.size)
    }
}
