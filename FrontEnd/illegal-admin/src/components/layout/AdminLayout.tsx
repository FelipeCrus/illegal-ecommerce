import { Outlet } from 'react-router-dom'
import '../../styles/admin.css'

export function AdminLayout() {
  return (
    <div className="admin-container">
      <aside className="admin-sidebar">
        <h2>Illegal Admin</h2>

      <nav>
        <a href="/admin/orders">Pedidos</a>
        <a href="/admin/users">Usuários</a>
      </nav>

      </aside>

      <main className="admin-content">
        <Outlet />
      </main>
    </div>
  )
}
