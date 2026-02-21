import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { LoginRequest } from '../../models/login.model';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';
  isLoading: boolean = false;  // <-- ДОБАВЬТЕ ЭТО
  
  private authService = inject(AuthService);
  private userService = inject(UserService);
  private router = inject(Router);

  onSubmit(): void {
    // Валидация
    if (!this.email || !this.password) {
      this.errorMessage = 'Заполните все поля';
      return;
    }
    
    this.isLoading = true;  // <-- ВКЛЮЧАЕМ ЗАГРУЗКУ
    this.errorMessage = '';
    
    const loginData: LoginRequest = {
      email: this.email,
      password: this.password
    };
    
    this.authService.login(loginData).pipe(
      switchMap(() => this.authService.getCurrentUser())
    ).subscribe({
      next: (user) => {
        console.log('Успешный вход:', user);
        this.isLoading = false;  // <-- ВЫКЛЮЧАЕМ ЗАГРУЗКУ
        this.router.navigate(['/profile']);
      },
      error: (error) => {
        console.error('Ошибка:', error);
        this.isLoading = false;  // <-- ВЫКЛЮЧАЕМ ЗАГРУЗКУ
        this.errorMessage = error.error?.message || 'Ошибка при входе';
      }
    });
  }
}