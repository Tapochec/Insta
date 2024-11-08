//используется для хранения и управления токенами авторизации и
// информацией о пользователе в сессионном хранилище браузера (sessionStorage)
import {Injectable} from '@angular/core';
import {User} from '../models/User';
import {Router} from '@angular/router';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  constructor(private router: Router) {
  }

  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string {
    const token = sessionStorage.getItem(TOKEN_KEY);
    if (token) {
      return token;
    } else {
      return ''; // Или другое значение по умолчанию
    }
  }

  public saveUser(user: User): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): any {
    const userStr = sessionStorage.getItem(USER_KEY);
    if (userStr) {
      return JSON.parse(userStr);
    } else {
      return null; // Или другое значение по умолчанию
    }
  }

  logOut(): void {
    window.sessionStorage.clear();
    this.router.navigate(['/login']); // Используем Router для перенаправления
  }
}
