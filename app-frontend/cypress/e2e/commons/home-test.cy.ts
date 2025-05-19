describe('Página de información de la empresa', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/'); 
  });

  it('Debe mostrar el título y subtítulo', () => {
    cy.contains('h1.title', 'Bienvenido').should('be.visible');
    cy.contains('p.subtitle', 'Área de contacto de la empresa:').should('be.visible');
  });

  it('Debe mostrar la tarjeta de información de empresa si settingsForm está presente', () => {
    cy.get('.card').should('exist');
  });

  it('Debe mostrar el logo si está disponible', () => {
    cy.get('img[alt="Logo de la empresa"]').should('exist');
  });

  it('Debe mostrar el encabezado de la tarjeta con el icono y texto', () => {
    cy.get('.card-header-title')
      .should('contain.text', 'Información de la Empresa')
      .find('mat-icon')
      .should('contain.text', 'business');
  });

  it('Debe mostrar el teléfono con extensión', () => {
    cy.get('.card-content')
      .contains('strong', 'Teléfono:')
      .parent()
      .should('contain.text', '+');
  });

  it('Debe mostrar el correo electrónico', () => {
    cy.get('.card-content')
      .contains('strong', 'Correo:')
      .parent()
      .should('contain.text', '@');
  });

  it('Debe mostrar la descripción de la empresa', () => {
    cy.get('.card-content')
      .contains('strong', 'Descripción:')
      .parent()
      .next()
      .should('not.be.empty');
  });
});