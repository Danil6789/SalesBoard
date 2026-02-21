// src/app/models/advert.model.ts
export interface Advert {
  id: number;
  title: string;
  description: string;
  price: number;
  categoryId: number;
  city: string;
  type: string; // например: 'PRODUCT' или 'SERVICE'
  status: 'ACTIVE' | 'INACTIVE' | 'PENDING';
  imageUrl?: string;
  createdAt?: string; // добавьте это поле
  updatedAt?: string; // добавьте это поле
}