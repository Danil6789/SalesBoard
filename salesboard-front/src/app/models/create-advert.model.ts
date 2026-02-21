export interface CreateAdvert {
  title: string;
  description: string;
  price: number;
  categoryId: number;
  city: string;
  type: string;
  imageUrl?: string;
}