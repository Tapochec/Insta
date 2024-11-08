import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../service/auth.service';
import {TokenStorageService} from '../../service/token-storage.service';
import {NotificationService} from '../../service/notification.service';
import {Router} from '@angular/router';
import {of, tap} from 'rxjs';
import {catchError} from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{

  public loginForm: FormGroup = new FormGroup({});

  constructor(private authService: AuthService,
              private tokenStorage: TokenStorageService,
              private notificationService: NotificationService,
              private router: Router,
              private fb: FormBuilder) {

    // Переход на главную страницу, если пользователь уже аутентифицирован
    if (this.tokenStorage.getUser()) {
      this.router.navigate(['main']).then(() => {
        // Успешная навигация
        console.log('Navigation to main was successful!');
      }).catch(err => {
        // Обработка ошибки навигации
        console.error('Error during navigation to main:', err);
      });
    }
  }

  ngOnInit(): void {
    this.loginForm = this.createLoginForm();
  }

  createLoginForm(): FormGroup {

    return this.fb.group({
      username: ['', Validators.compose([Validators.required])],
      password: ['', Validators.compose([Validators.required])],//создаем поле и валидации к нему
    })

  }

  submit(): void {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).pipe(
        tap(data => {
          console.log(data);
          this.tokenStorage.saveToken(data.token);
          this.tokenStorage.saveUser(data);
          this.notificationService.showSnackBar('Вы очень успешно зашли на сайт');

          // Обработка Promise
          this.router.navigate(['/']).then(() => {
            window.location.reload();
          }).catch(err => {
            console.error('Ошибка при навигации:', err);
          });
        }),
        catchError(error => {
          console.log(error);
          this.notificationService.showSnackBar(error.message);
          return of(null); // Возвращаем Observable для предотвращения ошибки в потоке
        })
      ).subscribe(); // Подписываемся на результат
    }
  }

}
