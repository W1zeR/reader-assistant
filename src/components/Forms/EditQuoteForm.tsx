"use client";

import { BookOpenIcon, HashtagIcon, PencilIcon } from "@heroicons/react/24/outline";
import Link from "next/link";
import { useSession } from "next-auth/react";
import { FormEvent, useEffect, useState } from "react";
import axios from "axios";
import getQuoteBookFromFormData from "@/utils/getQuoteBookFromFormData";
import getTagsFromFormData from "@/utils/getTagsFromFormData";
import { useRouter } from "next/navigation";

export default function EditQuoteForm({ id }: { id: string }) {
  const { data: session } = useSession();
  const router = useRouter();
  const [quote, setQuote] = useState(
    {
      content: "",
      book: {
        id: -1,
        title: "",
        authors: []
      },
      tags: [
        {
          id: -1,
          name: ""
        }
      ]
    }
  );
  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  useEffect(() => {
    axios.get(API_URL + `/quotes/${id}`,
      {
        headers: {
          Authorization: `Bearer ${session?.accessToken}`
        }
      }
    )
      .then(response => {
        setQuote(response.data);
      })
      .catch(error => {
        console.error(error);
      });
  });

  async function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const formData = new FormData(event.currentTarget);

    await axios.put(API_URL + `/quotes/${id}`, {
      content: formData.get("content"),
      book: getQuoteBookFromFormData(formData),
      tags: getTagsFromFormData(formData)
    }, {
      headers: {
        Authorization: `Bearer ${session?.accessToken}`
      }
    });

    router.push("/private");
  }

  return (
    <>
      <form onSubmit={onSubmit}>
        <div className="mb-8">
          <label
            htmlFor="content"
            className="mb-3 block text-sm font-medium text-dark dark:text-white"
          >
            Содержание
          </label>
          <textarea
            name="content"
            rows={5}
            className="border-stroke w-full resize-none rounded-sm border bg-[#f8f8f8] px-6 py-3 text-base
                      text-body-color outline-none focus:border-primary dark:border-transparent dark:bg-[#2C303B]
                      dark:text-body-color-dark dark:shadow-two dark:focus:border-primary dark:focus:shadow-none"
            defaultValue={quote.content}
          />
        </div>
        <div className="mb-8">
          <label
            htmlFor="book"
            className="mb-3 block text-sm text-dark dark:text-white"
          >
            {" "}
            <BookOpenIcon className="h-6 w-6 inline-block" /> Книга{" "}
          </label>
          <input
            type="text"
            name="book"
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
            defaultValue={quote.book.title}
          />
        </div>
        <div className="mb-8">
          <label
            htmlFor="authors"
            className="mb-3 block text-sm text-dark dark:text-white"
          >
            {" "}
            <PencilIcon className="h-6 w-6 inline-block" /> Авторы{" "}
          </label>
          <input
            type="text"
            name="authors"
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
            defaultValue={quote.book.authors.map(a => `${a.name} ${a.surname}${a.patronymic == null ?
              "" : " " + a.patronymic}`).join(", ")}
          />
        </div>
        <div className="mb-8">
          <label
            htmlFor="tags"
            className="mb-3 block text-sm text-dark dark:text-white"
          >
            {" "}
            <HashtagIcon className="h-6 w-6 inline-block" /> Теги{" "}
          </label>
          <input
            type="text"
            name="tags"
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
            defaultValue={quote.tags.map(t => t.name).join(", ")}
          />
        </div>
        <div className="mb-6">
          <button type="submit"
                  className="shadow-submit dark:shadow-submit-dark flex w-full items-center justify-center
                      rounded-sm bg-primary px-9 py-4 text-base font-medium text-white duration-300
                       hover:bg-primary/90">
            Сохранить
          </button>
        </div>
        <div className="mb-6">
          <button
            className="shadow-submit dark:shadow-submit-dark flex w-full items-center justify-center
                      rounded-sm bg-red-700 px-9 py-4 text-base font-medium text-white duration-300
                       hover:bg-red-700/90">
            <Link
              href="/private">
              Отмена
            </Link>
          </button>
        </div>
      </form>
    </>
  );
}