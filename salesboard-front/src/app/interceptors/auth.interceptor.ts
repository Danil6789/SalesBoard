import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  
  // Пропускаем запросы к auth (регистрация и логин) без токена
  if (req.url.includes('/auth/')) {
    return next(req).pipe(
      catchError((error) => {
        if (error.status === 401) {
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }
  
  // Для всех остальных запросов добавляем токен
  const token = localStorage.getItem('token');
  
  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log('🟢 Добавлен токен для:', req.url);
    return next(authReq).pipe(
      catchError((error) => {
        if (error.status === 401) {
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }
  
  console.log('🟡 Нет токена, пропускаем запрос:', req.url);
  return next(req).pipe(
    catchError((error) => {
      if (error.status === 401) {
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};