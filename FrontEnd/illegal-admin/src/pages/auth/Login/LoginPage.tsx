import { useState } from 'react'
import { api } from '../../../services/api'
import { useNavigate } from 'react-router-dom'
import '../../../styles/admin.css'

export function LoginPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  async function handleLogin(e: React.FormEvent) {
    e.preventDefault()

    try {
      const response = await api.post('/users/login', { email, password })
      localStorage.setItem('token', response.data.accessToken)
      navigate('/admin/orders')
    } catch (error: any) {
      alert(error.response?.data?.message || 'Erro no login')
    }
  }

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleLogin}>
        <h2>Admin Login</h2>

        <input
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
        />

        <input
          type="password"
          placeholder="Senha"
          value={password}
          onChange={e => setPassword(e.target.value)}
        />

        <button type="submit">Entrar</button>
      </form>
    </div>
  )
}
