import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AdvertService } from '../../services/advert.service';
import { User } from '../../models/user.model';
import { Advert } from '../../models/advert.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  userAdverts: Advert[] = [];
  isLoading: boolean = true;
  error: string = '';

  constructor(
    private authService: AuthService, 
    private advertService: AdvertService
  ) {}

  ngOnInit(): void {
    this.loadUserProfile();
    this.loadAdverts();
  }
  
  loadAdverts(): void {
    this.isLoading = true;
    this.advertService.getMyAdverts().subscribe({
      next: (adverts) => {
        this.userAdverts = adverts;
        this.isLoading = false;
        this.error = '';
      },
      error: (err) => {
        console.error('Ошибка загрузки объявлений:', err);
        this.error = 'Не удалось загрузить объявления';
        this.isLoading = false;
      }
    });
  }

  loadUserProfile(): void {
    this.user = this.authService.getStoredUser();
    
    if (!this.user && this.authService.isAuthenticated()) {
      this.authService.getCurrentUser().subscribe({
        next: (user) => {
          this.user = user;
        },
        error: (err) => {
          this.error = 'Не удалось загрузить данные профиля';
        }
      });
    }
  }
  
  getCategoryName(categoryId: number): string {
    const categories: { [key: number]: string } = {
      1: 'Недвижимость',
      2: 'Транспорт',
      3: 'Работа',
      4: 'Услуги',
      5: 'Электроника',
      6: 'Одежда',
      7: 'Мебель',
      8: 'Хобби',
      9: 'Животные',
      10: 'Спорт'
    };
    return categories[categoryId] || 'Другое';
  }

  getStatusText(status: string): string {
    const statusMap: { [key: string]: string } = {
      'ACTIVE': 'Активно',
      'INACTIVE': 'Неактивно', 
      'PENDING': 'На рассмотрении',
      'SOLD': 'Продано'
    };
    return statusMap[status] || status;
  }

  logout(): void {
    this.authService.logout();
    window.location.href = '/';
  }
}