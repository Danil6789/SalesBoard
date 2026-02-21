import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface UserDto {
  id: number;
  username: string;
  email: string;
  createdAt: string;  // LocalDateTime приходит как строка
}

export interface UpdateUserRequest {
  username?: string;
  email?: string;
}

export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) { }

  getUserById(id: number): Observable<UserDto> {
    return this.http.get<UserDto>(`${this.apiUrl}/${id}`);
  }


  updateUser(id: number, request: UpdateUserRequest): Observable<UserDto> {
    return this.http.put<UserDto>(`${this.apiUrl}/${id}`, request);
  }


  changePassword(id: number, request: ChangePasswordRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/change-password`, request);
  }


  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}