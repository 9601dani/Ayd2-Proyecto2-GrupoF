import { Pipe, PipeTransform } from '@angular/core';
import { environment } from '../../environments/environment';

@Pipe({
  name: 'image',
  standalone: true
})
export class ImagePipe implements PipeTransform {

  readonly BUCKET_URL = environment.BUCKET_URL;

  transform(value: string): string {
    return `${this.BUCKET_URL}/${value}`;
  }

}
