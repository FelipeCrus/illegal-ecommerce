import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { AdminLayout } from './components/layout/AdminLayout'
import { OrdersPage } from './pages/admin/Orders/OrdersPage'
import { UsersPage } from './pages/admin/Users/UsersPage'
import { LoginPage } from './pages/auth/Login/LoginPage'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route path="/admin" element={<AdminLayout />}>
          <Route path="orders" element={<OrdersPage />} />
          <Route path="users" element={<UsersPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
