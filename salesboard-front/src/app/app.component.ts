import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, RouterLink, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Salesboard';
  isLoggedIn = false;
  username = 'Иван';
  isHomePage = true;

  constructor(private router: Router) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.isHomePage = this.router.url === '/';
      this.checkAuthStatus();
    });
  }

  ngOnInit() {
    this.checkAuthStatus();
  }

  checkAuthStatus() {

    const token = localStorage.getItem('token');
    this.isLoggedIn = !!token;
    
  
    if (this.isLoggedIn) {
      const userStr = localStorage.getItem('user');
      if (userStr) {
        try {
          const user = JSON.parse(userStr);
          this.username = user.username || user.name || 'Пользователь';
        } catch (e) {
          console.error('Ошибка парсинга user:', e);
        }
      }
    }
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.isLoggedIn = false;
    this.router.navigate(['/']);
  }
}