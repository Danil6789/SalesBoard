// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, switchMap } from 'rxjs';
import { LoginRequest } from '../models/login.model';
import { RegisterRequest } from '../models/register.model';
import { JwtResponse } from '../models/jwt.model';
import { User } from '../models/user.model';
import { Advert } from '../models/advert.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(loginData: LoginRequest): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${this.apiUrl}/login`, loginData)
      .pipe(
        tap(response => {
          localStorage.setItem('token', response.token);
        })
      );
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/me`)
      .pipe(
        tap(user => {
          localStorage.setItem('user', JSON.stringify(user));
        })
      );
  }

  // Регистрация
  register(registerData: RegisterRequest): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register`, registerData);
  }
  // Получение сохраненного пользователя
  getStoredUser(): User | null {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  }

  // Проверка авторизации
  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  // Получение токена
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // Выход
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }
}