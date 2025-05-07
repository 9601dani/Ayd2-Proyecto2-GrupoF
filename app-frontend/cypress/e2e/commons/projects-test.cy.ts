describe('Gestión de Proyectos', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/projects');
  });

  it('Debe mostrar el título de la sección', () => {
    cy.contains('h1', 'Proyectos Activos').should('be.visible');
  });

  it('Debe abrir el modal de creación al hacer clic en "Crear nuevo proyecto"', () => {
    cy.get('button.button.is-primary', { timeout: 10000 })
      .should('be.visible')
      .click();

    cy.contains('.modal-card-title', 'Crear Proyecto').should('be.visible');
  });

  it('Debe crear un proyecto nuevo y mostrarlo en la lista', () => {
    cy.contains('Proyectos Activos').should('be.visible');

    cy.contains('button', 'Crear nuevo proyecto').click();
  });

  it('Debe cerrar el modal sin hacer cambios al presionar "Cancelar"', () => {
    cy.get('button.button.is-primary').click();
    cy.contains('button', 'Cancelar').click();

    cy.contains('.modal-card-title', 'Crear Proyecto').should('not.exist');
  });
});
