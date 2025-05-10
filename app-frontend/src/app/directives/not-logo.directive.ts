import {Directive, ElementRef, HostListener} from '@angular/core';

@Directive({
  selector: '[appNotLogo]',
  standalone: true
})
export class NotLogoDirective {

  constructor(private _elementImgRef: ElementRef) { }

  @HostListener('error')
  onError() :void{
    this._elementImgRef.nativeElement.src = "assets/default-logo.png";
  }
}
