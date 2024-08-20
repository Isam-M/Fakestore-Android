import androidx.lifecycle.ViewModel
import com.example.fakestoreapp.data.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderDetailsViewModel : ViewModel() {
    private val _selectedOrder = MutableStateFlow<Order?>(null)
    val selectedOrder: StateFlow<Order?> = _selectedOrder

    fun setOrder(order: Order) {
        _selectedOrder.value = order
    }
}
