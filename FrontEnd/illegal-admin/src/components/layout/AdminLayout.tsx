import { Outlet, NavLink } from 'react-router-dom'
import '../../styles/admin.css'

export function AdminLayout() {
  return (
    <div className="admin-container">
      <aside className="admin-sidebar">
        <h2>Illegal Admin</h2>

        <nav>
          <NavLink to="orders" className={({ isActive }) => isActive ? 'active' : ''}>
            Pedidos
          </NavLink>
          <NavLink to="users" className={({ isActive }) => isActive ? 'active' : ''}>
            Usuários
          </NavLink>
          <NavLink to="products" className={({ isActive }) => isActive ? 'active' : ''}>
            Produtos
          </NavLink>
        </nav>
      </aside>

      <main className="admin-content">
        <Outlet />
      </main>
    </div>
  )
}
