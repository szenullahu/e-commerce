export const AVAILABLE_AVATARS = [
  'AVATAR_1',
  'AVATAR_2',
  'AVATAR_3',
  'AVATAR_4',
  'AVATAR_5',
  'AVATAR_6',
] as const;

export type ProfilePictureOption = typeof AVAILABLE_AVATARS[number];

export const PROFILE_PICTURE_MAP: Record<ProfilePictureOption, string> = {
  AVATAR_1: 'avatar1.png',
  AVATAR_2: 'avatar2.png',
  AVATAR_3: 'avatar3.png',
  AVATAR_4: 'avatar4.png',
  AVATAR_5: 'avatar5.png',
  AVATAR_6: 'avatar6.png'
};
