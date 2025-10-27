export type QuoteStatus = {
  id: number
  name: string
};

export type QuoteBook = {
  id: number
  title: string
  authors: BookAuthor[]
};

export type BookAuthor = {
  id: number
  surname: string
  name: string
  patronymic: string | null
};

export type Quote = {
  id: number
  content: string
  book: QuoteBook
  status: QuoteStatus
  tags: Tag[]
  profile: Profile
  likes: number
  changeDate: string
};

export type Tag = {
  id: number
  name: string
};

export type Profile = {
  id: number
  email: string
  login: string
};
