import { useEffect, useState } from 'react'
import { api } from '../../../services/api'
import type { Order, OrderStatus  } from '../../../types/Admin/Order'



export function OrdersPage() {
  const [orders, setOrders] = useState<Order[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get('/admin/orders')
      .then(response => {
        console.log('Resposta /admin/orders:', response.data)
        setOrders(response.data)
      })
      .catch(error => {
        console.error('Erro ao buscar pedidos', error)
      })
      .finally(() => setLoading(false))
  }, [])

  const statusActions: Record<OrderStatus, { label: string; endpoint: string } | null> = {
  PAID: { label: 'Separar', endpoint: 'picking' },
  PICKING: { label: 'Enviar', endpoint: 'shipped' },
  SHIPPED: { label: 'Entregar', endpoint: 'delivered' },
  PENDING: null,
  DELIVERED: null,
  CANCELLED: null,
  REFUNDED: null,
}


async function handleUpdateStatus(orderId: number, endpoint: string) {
  const criticalActions: Record<string, string> = {
    refund: 'Deseja realmente estornar este pedido?',
    cancel: 'Deseja realmente cancelar este pedido?',
  }

  if (criticalActions[endpoint]) {
    const confirmed = window.confirm(criticalActions[endpoint])
    if (!confirmed) return
  }

  try {
    const response = await api.post(
      `/admin/orders/${orderId}/${endpoint}`
    )

    const updatedOrder: Order = response.data

    setOrders(prev =>
      prev.map(order =>
        order.id === updatedOrder.id ? updatedOrder : order
      )
    )
  } catch (error: any) {
    alert(error.response?.data?.message || 'Erro na operação')
  }
}




function canCancel(status: OrderStatus) {
  return status === 'PENDING'
}

function canRefund(status: OrderStatus) {
  return ['PAID', 'PICKING', 'SHIPPED', 'DELIVERED'].includes(status)
}



  return (
    <>
      <h1>Pedidos</h1>

      {loading ? (
        <p>Carregando pedidos...</p>
      ) : (
        <table className="orders-table">
  <thead>
    <tr>
      <th>ID</th>
      <th>Total</th>
      <th>Status</th>
      <th>Criado em</th>
      <th>Ações</th>
    </tr>
  </thead>

  <tbody>
    {orders.map(order => {
      const action = statusActions[order.status]

      return (
        <tr key={order.id}>
          <td>{order.id}</td>
          <td>R$ {order.total.toFixed(2)}</td>
          <td>{order.status}</td>
          <td>{new Date(order.createdAt).toLocaleString()}</td>

          <td>
            {action && (
              <button
                onClick={() => handleUpdateStatus(order.id, action.endpoint)}
              >
                {action.label}
              </button>
            )}

            {canCancel(order.status) && (
              <button
                onClick={() => handleUpdateStatus(order.id, 'cancel')}
              >
                Cancelar
              </button>
            )}

            {canRefund(order.status) && (
              <button
                onClick={() => handleUpdateStatus(order.id, 'refund')}
              >
                Estornar
              </button>
            )}
          </td>
        </tr>
      )
    })}
  </tbody>
</table>

      )}
    </>
  )
}
