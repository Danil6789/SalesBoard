import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AdvertService } from '../../services/advert.service';

interface Category {
  id: number;
  name: string;
}

@Component({
  selector: 'app-create-advert',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './create-advert.component.html',
  styleUrls: ['./create-advert.component.css']
})
export class CreateAdvertComponent {
  advert = {
    title: '',
    description: '',
    price: 0,
    categoryId: 0,
    city: '',
    type: '',
    imageUrl: ''
  };
  
  categories: Category[] = [
    { id: 1, name: 'Электроника' },
    { id: 2, name: 'Одежда' },
    { id: 3, name: 'Мебель' },
    { id: 4, name: 'Автомобили' },
    { id: 5, name: 'Недвижимость' },
    { id: 6, name: 'Работа' },
    { id: 7, name: 'Услуги' },
    { id: 8, name: 'Хобби' },
    { id: 9, name: 'Животные' },
    { id: 10, name: 'Спорт' }
  ];

  selectedFiles: { file: File; preview: string; }[] = [];
  isSubmitting = false;
  errorMessage = '';

  constructor(
    private advertService: AdvertService,
    private router: Router
  ) {}

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    
    const files = event.dataTransfer?.files;
    if (files) {
      this.handleFiles(files);
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.handleFiles(input.files);
    }
  }

  handleFiles(files: FileList): void {
    Array.from(files).forEach(file => {
      if (file.size > 10 * 1024 * 1024) {
        this.errorMessage = 'Файл слишком большой. Максимальный размер 10MB';
        setTimeout(() => this.errorMessage = '', 3000);
        return;
      }

      if (!file.type.startsWith('image/')) {
        this.errorMessage = 'Можно загружать только изображения';
        setTimeout(() => this.errorMessage = '', 3000);
        return;
      }

      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.selectedFiles.push({
          file: file,
          preview: e.target.result
        });
      };
      reader.readAsDataURL(file);
    });
  }

  removeImage(index: number): void {
    this.selectedFiles.splice(index, 1);
  }

  onSubmit(): void {
    if (this.isSubmitting) return;

    // Валидация цены
    if (this.advert.price < 0) {
      this.errorMessage = 'Цена не может быть отрицательной';
      setTimeout(() => this.errorMessage = '', 3000);
      return;
    }

    this.isSubmitting = true;
    this.errorMessage = '';
    
    this.advertService.createAdvert(this.advert).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        this.router.navigate(['/adverts']);
      },
      error: (error) => {
        console.error('Ошибка:', error);
        this.errorMessage = error.error?.message || 'Ошибка при создании объявления';
        this.isSubmitting = false;
        setTimeout(() => this.errorMessage = '', 3000);
      }
    });
  }
}