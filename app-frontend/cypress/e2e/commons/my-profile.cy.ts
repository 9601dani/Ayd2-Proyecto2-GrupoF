describe('Perfil de Usuario', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/profile'); 

    cy.setCookie('TOKEN', 'eyJhbGciOiJIUzI1NiIsInR...');

    cy.setCookie('REFRESH_TOKEN', 'eyJh...');

    window.localStorage.setItem('USER_ID', '123'); 

  });

  it('Debe cargar el formulario de usuario correctamente', () => {
    cy.get('input[name="username"]').should('exist').and('have.attr', 'readonly');
    cy.get('input[name="email"]').should('exist');
    cy.get('input[name="firstName"]').should('exist');
    cy.get('input[name="lastName"]').should('exist');
    cy.get('input[name="password"]').should('exist');
    cy.get('button[type="submit"]').contains('Actualizar información');
  });

  it('Debe mostrar imagen de perfil por defecto o previsualización', () => {
    cy.get('img[alt="Foto de perfil"]').should('exist');
  });

  it('Debe subir una imagen y activar botón de "Actualizar foto"', () => {
    const imagePath = 'perfil.jpg'; 

    cy.get('input[type="file"]').selectFile(`cypress/fixtures/${imagePath}`, { force: true });
    cy.get('button').contains('Actualizar foto').should('not.be.disabled');
  });

  it('Debe deshabilitar el botón de "Actualizar información" si hay campos vacíos', () => {
    cy.get('input[name="email"]').clear();
    cy.get('input[name="firstName"]').clear();
    cy.get('input[name="lastName"]').clear();

    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Debe permitir enviar el formulario cuando los datos son válidos', () => {
    cy.get('input[name="email"]').clear().type('nuevo@email.com');
    cy.get('input[name="firstName"]').clear().type('NuevoNombre');
    cy.get('input[name="lastName"]').clear().type('ApellidoNuevo');

    cy.get('button[type="submit"]').should('not.be.disabled').click();

    cy.intercept('POST', '/api/user/update').as('updateUser');
    cy.wait('@updateUser');

  });

  it('Debe alternar visibilidad de la contraseña', () => {
    cy.get('input[name="password"]').type('secreta');
    cy.get('mat-icon').should('exist').click();
    cy.get('input[name="password"]').should('have.attr', 'type', 'text');
  });
});
