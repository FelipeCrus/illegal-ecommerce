import { useState, useEffect } from 'react'
import { api } from '../../../services/api'
import type { ProductResponseDTO } from '../../../types/Product'

type Props = {
  product?: ProductResponseDTO 
  onSave: (product: ProductResponseDTO) => void
  onCancel: () => void
}

export function AdminProductForm({ product, onSave, onCancel }: Props) {
  const [name, setName] = useState(product?.name || '')
  const [description, setDescription] = useState(product?.description || '')
  const [price, setPrice] = useState(product?.price || 0)
  const [stock, setStock] = useState(product?.stock || 0)
  const [imageUrl, setImageUrl] = useState(product?.imageUrl || '')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setLoading(true)

    try {
      let response
      if (product) {
        response = await api.put<ProductResponseDTO>(`/admin/products/${product.id}`, {
          name, description, price, stock, imageUrl
        })
      } else {
        response = await api.post<ProductResponseDTO>('/admin/products', {
          name, description, price, stock, imageUrl
        })
      }

      onSave(response.data)
    } catch (error: any) {
      console.error('Erro ao salvar produto', error)
      alert(error.response?.data?.message || 'Erro ao salvar produto')
    } finally {
      setLoading(false)
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>Nome:</label>
        <input value={name} onChange={e => setName(e.target.value)} required />
      </div>
      <div>
        <label>Descrição:</label>
        <input value={description} onChange={e => setDescription(e.target.value)} required />
      </div>
      <div>
        <label>Preço:</label>
        <input type="number" value={price} onChange={e => setPrice(+e.target.value)} required />
      </div>
      <div>
        <label>Estoque:</label>
        <input type="number" value={stock} onChange={e => setStock(+e.target.value)} required />
      </div>
      <div>
        <label>Imagem URL:</label>
        <input value={imageUrl} onChange={e => setImageUrl(e.target.value)} required />
      </div>

      <button type="submit" disabled={loading}>
        {loading ? 'Salvando...' : product ? 'Atualizar' : 'Criar'}
      </button>
      <button type="button" onClick={onCancel} disabled={loading}>Cancelar</button>
    </form>
  )
}
