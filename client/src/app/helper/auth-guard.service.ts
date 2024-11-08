//используется для реализации механизма авторизации,
// то есть проверки, имеет ли пользователь право доступа к определенному маршруту
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { TokenStorageService } from '../service/token-storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(private router: Router,
              private tokenService: TokenStorageService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
    Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const currentUser = this.tokenService.getUser();
    if (currentUser) {
      return true;
    }

    // Перенаправляем пользователя на страницу входа *перед* возвратом `false`
    this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } })
      .then(() => {
        // Дополнительный код, который нужно выполнить после перенаправления
      });

    return false; // Возвращаем `false`, чтобы заблокировать доступ к странице
  }
}
