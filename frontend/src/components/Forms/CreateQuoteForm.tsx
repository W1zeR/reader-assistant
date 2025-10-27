"use client";

import { BookOpenIcon, HashtagIcon, PencilIcon } from "@heroicons/react/24/outline";
import Link from "next/link";
import React, { FormEvent } from "react";
import axios from "axios";
import { useSession } from "next-auth/react";
import getQuoteBookFromFormData from "@/utils/getQuoteBookFromFormData";
import getTagsFromFormData from "@/utils/getTagsFromFormData";
import { useRouter } from "next/navigation";

export default function CreateQuoteForm() {
  const API_URL = process.env.NEXT_PUBLIC_API_URL;
  const { data: session } = useSession();
  const router = useRouter();

  async function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const formData = new FormData(event.currentTarget);

    await axios.post(API_URL + `/quotes`, {
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
            placeholder="Введите текст цитаты"
            className="border-stroke w-full resize-none rounded-sm border bg-[#f8f8f8] px-6 py-3 text-base
                      text-body-color outline-none focus:border-primary dark:border-transparent dark:bg-[#2C303B]
                      dark:text-body-color-dark dark:shadow-two dark:focus:border-primary dark:focus:shadow-none"
          ></textarea>
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
            placeholder="Введите название книги"
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
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
            placeholder="Введите ФИО авторов через запятые"
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
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
            placeholder="Введите теги через запятые"
            className="border-stroke dark:text-body-color-dark dark:shadow-two w-full rounded-sm border
                      bg-[#f8f8f8] px-6 py-3 text-base text-body-color outline-none transition-all duration-300
                      focus:border-primary dark:border-transparent dark:bg-[#2C303B] dark:focus:border-primary
                      dark:focus:shadow-none"
          />
        </div>
        <div className="mb-6">
          <button type="submit"
                  className="shadow-submit dark:shadow-submit-dark flex w-full items-center justify-center
                      rounded-sm bg-primary px-9 py-4 text-base font-medium text-white duration-300
                      hover:bg-primary/90">
            Добавить
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