import { useEffect, useState } from 'react'
import { api } from '../../../services/api'
import type { User } from '../../../types/Admin/User'

export function UsersPage() {
  const [users, setUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
  api.get('/admin/users')
    .then(response => {
      const normalizedUsers = response.data.map((u: any) => ({
        ...u,
        role: u.role.replace('ROLE_', '') as 'USER' | 'ADMIN'
      }))
      setUsers(normalizedUsers)
    })
    .catch(() => alert('Erro ao buscar usuários'))
    .finally(() => setLoading(false))
}, [])



  
  async function handleToggleRole(userId: number) {
    const user = users.find(u => u.id === userId)
    if (!user) return


    
    const action = user.role === 'USER' ? 'promote' : 'demote'
    const confirmed = window.confirm(
      `Deseja realmente ${action === 'promote' ? 'promover' : 'despromover'} este usuário?`
    )
    if (!confirmed) return

    try {
      const response = await api.post(`/admin/users/${userId}/${action}`)
      const updatedUser: User = response.data

      setUsers(prev => prev.map(u => u.id === updatedUser.id ? { ...u, role: updatedUser.role } : u))
    } catch (error: any) {
      alert(error.response?.data?.message || 'Erro na operação')
    }
  }

  async function handleToggleBlocked(userId: number) {
    const user = users.find(u => u.id === userId)
    if (!user) return

    const action = user.blocked ? 'unblock' : 'block'
    const confirmed = window.confirm(
      `Deseja realmente ${user.blocked ? 'desbloquear' : 'bloquear'} este usuário?`
    )
    if (!confirmed) return

    try {
      const response = await api.post(`/admin/users/${userId}/${action}`)
      const updatedUser: User = response.data

      setUsers(prev => prev.map(u => u.id === updatedUser.id ? { ...u, blocked: updatedUser.blocked } : u))
    } catch (error: any) {
      alert(error.response?.data?.message || 'Erro na operação')
    }
  }

  return (
    <>
      <h1>Usuários</h1>

      {loading ? (
        <p>Carregando usuários...</p>
      ) : (
        <table className="users-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Email</th>
              <th>Role</th>
              <th>Criado em</th>
              <th>Ações</th>
            </tr>
          </thead>

          <tbody>
            {users.map(user => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.name}</td>
                <td>{user.email}</td>
                <td>{user.role}</td>
                <td>{new Date(user.createdAt).toLocaleString()}</td>
                <td>
                  <button onClick={() => handleToggleRole(user.id)}>
                    {user.role === 'USER' ? 'Promover' : 'Despromover'}
                  </button>
                  <button onClick={() => handleToggleBlocked(user.id)} style={{ marginLeft: '10px' }}>
                    {user.blocked ? 'Desbloquear' : 'Bloquear'}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </>
  )
}
