export default function getTagsFromFormData(formData: FormData) {
  const tagsString = formData.get("tags").toString();
  const tags = tagsString.split(",");
  return tags.map(t => {
    return { name: t.trim() };
  });
}