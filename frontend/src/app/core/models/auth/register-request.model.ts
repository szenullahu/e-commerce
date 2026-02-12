import {ProfilePictureOption} from '../user/profile-picture.enum';

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  surname: string;
  profilePicture: ProfilePictureOption;
}
