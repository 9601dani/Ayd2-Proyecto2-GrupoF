import { StyleDictionary, TDocumentDefinitions } from "pdfmake/interfaces";
import { LocalStorageService } from "../services/commons/local-storage.service";

export type ColumnDefinition<T> = {
  header: string;
  field: keyof T;
};

export type CompanyInfo = {
  companyName: string;
  companyAddress: string;
  companyPhoneNumber: string;
  emailAddress: string;
};


export async function generateReportPDF<T>(
  columns: ColumnDefinition<T>[],
  data: T[],
  company: CompanyInfo,
  logoUrl: string,
  currency: string,
  fecha: string,
  username:String,
  footerFields?: { label: string, value: number, isCurrency?:boolean }[]

) {
  const pdfMake = await import('pdfmake/build/pdfmake');
  const pdfFonts = await import('pdfmake/build/vfs_fonts');
  (pdfMake as any).vfs = (pdfFonts as any).vfs;

  const tableBody = [
    columns.map(col => ({ text: col.header, style: "tableHeader" })),
    ...data.map(row =>
      columns.map(col =>
        row[col.field] !== null && row[col.field] !== undefined
          ? `${row[col.field]}`
          : ""
      )
    ),
  ];

  const content: any[] = [];

  content.push({
    columns: [
      { image: logoUrl, width: 60 },
      {
        stack: [
          { text: company.companyName, style: "header" },
          { text: company.companyAddress, style: "subheader" },
          { text: `Tel: ${company.companyPhoneNumber}`, style: "subheader" },
          { text: `Email: ${company.emailAddress}`, style: "subheader" },
          { text: `Fecha: ${fecha}`, style: "subheader" },
          { text: `Usuario que genero reporte: ${username}`, style: "subheader" },
        ],
        alignment: "right",
      },
    ],
    margin: [0, 0, 0, 10],
  });

  content.push({
    table: {
      headerRows: 1,
      widths: Array(columns.length).fill("*"),
      body: tableBody,
    },
    layout: "lightHorizontalLines",
  });

  if (footerFields && footerFields.length > 0) {
    footerFields.forEach(field => {
      const formattedValue = field.isCurrency
        ? `${currency} ${field.value.toFixed(2)}`
        : field.value.toString();
  
      content.push({
        columns: [
          { text: "", width: "*" },
          {
            text: `${field.label}: ${formattedValue}`,
            style: "total",
            alignment: "right",
            margin: [0, 10, 0, 0],
          },
        ],
      });
    });
  }  

  const styles: StyleDictionary = {
    header: {
      fontSize: 16,
      bold: true,
    },
    subheader: {
      fontSize: 10,
      margin: [0, 2, 0, 2],
    },
    tableHeader: {
      bold: true,
      fontSize: 11,
      color: "black",
    },
    total: {
      fontSize: 12,
      bold: true,
    },
  };

  const docDefinition: TDocumentDefinitions = {
    content,
    styles,
  };

  pdfMake.createPdf(docDefinition).open();
  
}

export function getBase64ImageFromUrl(url: string): Promise<string> {
  return fetch(url)
    .then(response => {
      if (!response.ok) throw new Error("No se pudo cargar la imagen");
      return response.blob();
    })
    .then(blob => {
      return new Promise<string>((resolve, reject) => {
        const reader = new FileReader();
        reader.onloadend = () => resolve(reader.result as string);
        reader.onerror = reject;
        reader.readAsDataURL(blob); 
      });
    });
}

  

