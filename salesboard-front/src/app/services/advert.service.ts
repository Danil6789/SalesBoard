// src/app/services/advert.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Advert } from '../models/advert.model';
import { CreateAdvert } from '../models/create-advert.model';

@Injectable({
  providedIn: 'root'
})
export class AdvertService {
  private apiUrl = 'http://localhost:8080/api/adverts';

  constructor(private http: HttpClient) {}

  getAllAdverts(): Observable<Advert[]> {
    return this.http.get<Advert[]>(this.apiUrl);
  }

  getAdvertById(id: number): Observable<Advert> {
    return this.http.get<Advert>(`${this.apiUrl}/${id}`);
  }

  createAdvert(advert: CreateAdvert): Observable<Advert> {
    return this.http.post<Advert>(this.apiUrl, advert);
  }

  updateAdvert(id: number, advert: Advert): Observable<Advert> {
    return this.http.put<Advert>(`${this.apiUrl}/${id}`, advert);
  }

  deleteAdvert(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getMyAdverts(): Observable<Advert[]> {
    return this.http.get<Advert[]>(`${this.apiUrl}/my`);
  }

  searchAdverts(query: string): Observable<Advert[]> {
    return this.http.get<Advert[]>(`${this.apiUrl}/search`, {
      params: { q: query }
    });
  }

  getAdvertsByCategory(categoryId: number): Observable<Advert[]> {
    return this.http.get<Advert[]>(`${this.apiUrl}/category/${categoryId}`);
  }

  updateAdvertStatus(id: number, status: string): Observable<Advert> {
    return this.http.patch<Advert>(`${this.apiUrl}/${id}/status`, { status });
  }
}