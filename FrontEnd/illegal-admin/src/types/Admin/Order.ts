export type OrderStatus =
  | 'PENDING'
  | 'PAID'
  | 'PICKING'
  | 'SHIPPED'
  | 'DELIVERED'
  | 'CANCELLED'
  | 'REFUNDED'

export interface Order {
  id: number
  total: number
  status: OrderStatus
  createdAt: string
}
