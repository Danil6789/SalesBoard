// src/app/components/adverts/adverts.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AdvertService } from '../../services/advert.service';
import { Advert } from '../../models/advert.model';

@Component({
  selector: 'app-adverts',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './adverts.component.html',
  styleUrls: ['./adverts.component.css']
})
export class AdvertsComponent implements OnInit {
  adverts: Advert[] = [];
  filteredAdverts: Advert[] = [];
  searchQuery: string = '';
  isLoading: boolean = true;

  constructor(private advertService: AdvertService) {}

  ngOnInit(): void {
    this.loadAdverts();
  }

  loadAdverts(): void {
    this.advertService.getAllAdverts().subscribe({
      next: (data) => {
        this.adverts = data;
        this.filteredAdverts = data;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Ошибка загрузки объявлений:', error);
        this.isLoading = false;
      }
    });
  }

  onSearch(): void {
    if (this.searchQuery.trim()) {
      this.advertService.searchAdverts(this.searchQuery).subscribe({
        next: (data) => {
          this.filteredAdverts = data;
        }
      });
    } else {
      this.filteredAdverts = this.adverts;
    }
  }

  getCategoryName(categoryId: number): string {
    const categories: { [key: number]: string } = {
      1: 'Электроника',
      2: 'Одежда',
      3: 'Мебель',
      4: 'Автомобили',
      5: 'Недвижимость',
      6: 'Работа',
      7: 'Услуги',
      8: 'Хобби',
      9: 'Животные',
      10: 'Спорт'
    };
    return categories[categoryId] || 'Другое';
  }
}