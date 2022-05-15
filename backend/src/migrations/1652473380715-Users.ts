import { MigrationInterface, QueryRunner } from 'typeorm';

export class Users1652473380715 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    queryRunner.query(
      'CREATE TABLE user(id INTEGER PRIMARY KEY AUTOINCREMENT, email VARCHAR(64) UNIQUE NOT NULL, password VARCHAR(64) NOT NULL, salt VARCHAR(64) NOT NULL)',
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    throw Error('No Reverse');
  }
}
