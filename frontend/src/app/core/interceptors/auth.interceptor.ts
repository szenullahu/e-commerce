import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import {AuthService} from '../services/auth/auth.service';


export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token) {
    const authReq = req.clone({


      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log('token in interceptor:', token);
    console.log('url:', req.url);

    return next(authReq);
  }

  return next(req);
};
