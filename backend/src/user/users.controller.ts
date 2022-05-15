import { Controller, Get, Post, Body } from '@nestjs/common';
import { IsEmail, IsString } from 'class-validator';
import { MessageDTO } from 'src/types';
import { UsersService } from './users.service';

class CredentialsDTO {
  @IsEmail()
  email: string;

  @IsString()
  password: string;
}

@Controller({ path: '/api/v0/user' })
export class UsersController {
  constructor(private readonly usersService: UsersService) {}

  @Post('register')
  async register(@Body() credentialsDTO: CredentialsDTO): Promise<MessageDTO> {
    return this.usersService.register(credentialsDTO).then(() => {
      return { message: 'Success' };
    });
  }

  @Post('login')
  async login(@Body() credentialsDTO: CredentialsDTO): Promise<MessageDTO> {
    return this.usersService.login(credentialsDTO).then(() => {
      return { message: 'Success' };
    });
  }

  @Get('users')
  async get() {
    return this.usersService.findAll();
  }
}
