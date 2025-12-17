import { useEffect, useState } from 'react'
import { api } from '../../../services/api'
import type { ProductResponseDTO } from '../../../types/Product'
import { AdminProductForm } from './AdminProductForm'

export function AdminProductsPage() {
  const [products, setProducts] = useState<ProductResponseDTO[]>([])
  const [loading, setLoading] = useState(true)
  const [editingProduct, setEditingProduct] = useState<ProductResponseDTO | null>(null)
  const [showForm, setShowForm] = useState(false)

  useEffect(() => {
    fetchProducts()
  }, [])

  async function fetchProducts() {
    try {
      const response = await api.get<ProductResponseDTO[]>('/products')
      setProducts(response.data)
    } catch (error) {
      console.error('Erro ao buscar produtos', error)
      alert('Erro ao buscar produtos')
    } finally {
      setLoading(false)
    }
  }

  function handleEdit(product: ProductResponseDTO) {
    setEditingProduct(product)
    setShowForm(true)
  }

  function handleCreate() {
    setEditingProduct(null)
    setShowForm(true)
  }

  function handleCancel() {
    setShowForm(false)
  }

  function handleSave(savedProduct: ProductResponseDTO) {
    setProducts(prev => {
      const exists = prev.find(p => p.id === savedProduct.id)
      if (exists) {
        return prev.map(p => p.id === savedProduct.id ? savedProduct : p)
      } else {
        return [...prev, savedProduct]
      }
    })
    setShowForm(false)
  }

  async function handleDelete(id: number) {
    const confirmed = window.confirm('Deseja realmente deletar este produto?')
    if (!confirmed) return

    try {
      await api.delete(`/admin/products/${id}`)
      setProducts(prev => prev.filter(p => p.id !== id))
    } catch (error: any) {
      console.error('Erro ao deletar produto', error)
      alert(error.response?.data?.message || 'Erro ao deletar produto')
    }
  }

  if (showForm) {
    return (
      <AdminProductForm
        product={editingProduct || undefined}
        onSave={handleSave}
        onCancel={handleCancel}
      />
    )
  }

  return (
    <>
      <h1>Produtos</h1>
      <button onClick={handleCreate}>Novo Produto</button>

      {loading ? (
        <p>Carregando produtos...</p>
      ) : (
        <table className="orders-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Descrição</th>
              <th>Preço</th>
              <th>Estoque</th>
              <th>Ações</th>
            </tr>
          </thead>

          <tbody>
            {products.map(product => (
              <tr key={product.id}>
                <td>{product.id}</td>
                <td>{product.name}</td>
                <td>{product.description}</td>
                <td>R$ {product.price.toFixed(2)}</td>
                <td>{product.stock}</td>
                <td>
                  <button onClick={() => handleEdit(product)}>Editar</button>
                  <button onClick={() => handleDelete(product.id)}>Deletar</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </>
  )
}
