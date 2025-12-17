import { useEffect, useState } from 'react'
import { api } from '../../../services/api'
import type { User } from '../../../types/Admin/User'

export function UsersPage() {
  const [users, setUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get('/admin/users')
      .then(response => {
        setUsers(response.data)
      })
      .catch(() => {
        alert('Erro ao buscar usuários')
      })
      .finally(() => setLoading(false))
  }, [])

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
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </>
  )
}
