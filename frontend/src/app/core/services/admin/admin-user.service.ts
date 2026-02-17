import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {User} from '../../models/user/user.model';
import {Observable} from 'rxjs';
import {RegisterAdmin} from '../../models/admin/register-admin.model';
import {MessageResponseDto} from '../../models/auth/message-response.dto';

@Injectable({
  providedIn: 'root',
})
export class AdminUserService {
  private http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/admin'

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/users`);
  }

  getUserById( id: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/${id}`);
  }

  createAdmin(data : RegisterAdmin): Observable<MessageResponseDto>{
    return this.http.post<MessageResponseDto>(`${this.apiUrl}/create-admin`, data);
  }

  deleteUser(id: string): Observable<MessageResponseDto> {
    return this.http.delete<MessageResponseDto>(`${this.apiUrl}/users/${id}`);
  }

}
