export default function getQuoteBookFromFormData(formData: FormData) {
  const authorsString = formData.get("authors").toString();
  const authorsSplittedByComma = authorsString.split(",");
  const authorsSplittedBySpace = authorsSplittedByComma.map(a => a.trim().split(" ", 3));
  const authors = authorsSplittedBySpace.map(snp => {
    return {
      surname: snp[0] || "",
      name: snp[1] || "",
      patronymic: snp[2] || ""
    };
  });

  return {
    title: formData.get("book"),
    authors: authors
  };
}