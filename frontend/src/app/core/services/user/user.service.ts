import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Observable, tap} from 'rxjs';
import {User} from '../../models/user/user.model';
import {UpdateProfileInfoDto} from '../../models/user/update-profile-info.dto';
import {UpdateEmailDto} from '../../models/user/update-email.dto';
import {UpdatePasswordDto} from '../../models/user/update-password.dto';
import {UpdateProfilePictureDto} from '../../models/user/update-profile-picture.dto';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private http = inject(HttpClient);
  private router = inject(Router);

  private readonly apiUrl = 'http://localhost:8080/api/user'

  private refreshComponent() {
    const currentUrl = this.router.url;    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigate([currentUrl]);
    });
  }
  getProfile():Observable<User> {
    return this.http.get<User>(`${this.apiUrl}`);
  }

  updateProfileInfo(dto: UpdateProfileInfoDto): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/profile`, dto).pipe(
      tap(() => this.refreshComponent())
    );
  }

  updateProfilePicture(dto: UpdateProfilePictureDto): Observable<User> {
    return this.http.patch<User>(`${this.apiUrl}/profile-picture`, dto).pipe(
      tap(() => this.refreshComponent())
    );
  }

  updateEmail(dto: UpdateEmailDto): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/email`, dto).pipe(
      tap(() => this.refreshComponent())
    );
  }

  updatePassword(dto: UpdatePasswordDto): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/password`, dto).pipe(
      tap(() => this.refreshComponent())
    );
  }

}
