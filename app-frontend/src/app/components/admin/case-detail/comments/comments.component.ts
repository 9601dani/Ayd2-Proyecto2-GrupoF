import {Component, inject, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {QuillModule} from 'ngx-quill';
import {FormControl, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatDivider} from '@angular/material/divider';
import {ImagePipe} from '../../../../pipes/image.pipe';
import {NotProfileDirective} from '../../../../directives/not-profile.directive';
import {LocalStorageService} from '../../../../services/commons/local-storage.service';
import {HtmlSanitizerPipe} from '../../../../pipes/html-sanitizer.pipe';
import {ProjectService} from '../../../../services/project/project.service';
import {AlertService} from '../../../../services/commons/alert.service';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-comments',
  standalone: true,
  imports: [QuillModule,
    ReactiveFormsModule,
    MatDivider,
    ImagePipe,
    NotProfileDirective,
    HtmlSanitizerPipe, DatePipe
  ],
  templateUrl: './comments.component.html',
  styleUrl: './comments.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class CommentsComponent implements OnInit {

  @Input() caseId: number = 0;
  userId: number = 0;

  private _localStorageService: LocalStorageService = inject(LocalStorageService);
  private _projectService: ProjectService = inject(ProjectService);
  private _alertService: AlertService = inject(AlertService);
  editorContent = new FormControl("", Validators.required);
  replyEditors: { [key: number]: FormControl } = {};
  activeReplyId: number | null = null;
  photo: string = '';
  comments: any[] = [];

  constructor() {
    this.photo = this._localStorageService.getItem(this._localStorageService.USER_PHOTO);
    this.userId = this._localStorageService.getItem(this._localStorageService.USER_ID);
  }

  ngOnInit() {
    this.getCommentsByCaseId(this.caseId);
  }

  getCommentsByCaseId(id: number, idParent: number | null = null) {
    this._projectService.getCommentsByCaseAndParentId(id, idParent).subscribe({
      next: (response: any) => {
        console.log(response);
        if(idParent) {
          const comment = this.comments.find(c => c.id === idParent);
          comment.replies = response;
          console.log(comment);
          return;
        }
        this.comments = response;

      },
      error: (error: any) => {
        console.log(error);
      }
    })
  }

  saveComment(id?: number) {
    const contentControl = id ? this.replyEditors[id] : this.editorContent;
    const value = contentControl?.value;


    if (!value) return;

    const data = {
      idCase: this.caseId,
      idUser: this.userId,
      createdAt: new Date().toISOString(),
      content: value,
      idParent: id ?? null
    };

    this._projectService.saveComment(data).subscribe({
      next: (response: any) => {
        this._alertService.success("Éxito!", "Comentario creado con éxito.");
        if (id) {
          this.getCommentsByCaseId(this.caseId, id);
          this.activeReplyId = null;
          this.replyEditors[id].reset();
          return;
        }
        this.editorContent.reset();
        this.getCommentsByCaseId(this.caseId);
      },
      error: (error: any) => {
        this._alertService.error("Error!", error.error.message);
      }
    });
  }

  toggleReplyEditor(commentId: number) {
    if (this.activeReplyId === commentId) {
      this.activeReplyId = null;
    } else {
      this.activeReplyId = commentId;
      if (!this.replyEditors[commentId]) {
        this.replyEditors[commentId] = new FormControl("", Validators.required);
      }
    }
  }
}
