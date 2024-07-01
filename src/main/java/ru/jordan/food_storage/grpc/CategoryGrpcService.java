package ru.jordan.food_storage.grpc;

import category.CategoryServiceGrpc;
import category.ErrorCodeOuterClass;
import category.ExceptionResponseOuterClass;
import com.example.category.CategoryIdRequest;
import com.example.category.CategoryRequest;
import com.example.category.CategoryResponse;
import com.example.category.CategoryResponseWithException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.jordan.food_storage.model.Category;
import ru.jordan.food_storage.repository.CategoryRepository;

import com.google.protobuf.Timestamp;
import java.time.ZoneId;
import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class CategoryGrpcService extends CategoryServiceGrpc.CategoryServiceImplBase {

    private final CategoryRepository categoryRepository;

    @Override
    public void createCategory(CategoryRequest request, StreamObserver<CategoryResponseWithException> responseObserver) {
        try {
            if (categoryRepository.existsByName(request.getName())) {
                throw new RuntimeException("Категория с таким именем уже существует");
            }

            Category category = new Category();
            category.setName(request.getName());
            category.setDescription(request.getDescription());
            category.setActive(request.getActive());

            Category savedCategory = categoryRepository.save(category);

            CategoryResponse response = CategoryResponse.newBuilder()
                    .setCategory(toProto(savedCategory))
                    .build();

            responseObserver.onNext(CategoryResponseWithException.newBuilder().setCategoryResponse(response).build());
        } catch (Exception e) {
            ExceptionResponseOuterClass.ExceptionResponse exceptionResponse = ExceptionResponseOuterClass.ExceptionResponse.newBuilder()
                    .setCode(ErrorCodeOuterClass.ErrorCode.CATEGORY_ALREADY_EXISTS)
                    .setMessage(e.getMessage())
                    .build();
            responseObserver.onNext(CategoryResponseWithException.newBuilder().setExceptionResponse(exceptionResponse).build());
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getCategory(CategoryIdRequest request, StreamObserver<CategoryResponseWithException> responseObserver) {
        try {
            Optional<Category> categoryOptional = categoryRepository.findById(request.getId());
            if (categoryOptional.isPresent()) {
                Category category = categoryOptional.get();
                CategoryResponse response = CategoryResponse.newBuilder()
                        .setCategory(toProto(category))
                        .build();
                responseObserver.onNext(CategoryResponseWithException.newBuilder().setCategoryResponse(response).build());
            } else {
                throw new IllegalArgumentException("Category with id " + request.getId() + " not found");
            }
        } catch (Exception e) {
            ExceptionResponseOuterClass.ExceptionResponse exceptionResponse = ExceptionResponseOuterClass.ExceptionResponse.newBuilder()
                    .setCode(ErrorCodeOuterClass.ErrorCode.CATEGORY_NOT_FOUND)
                    .setMessage(e.getMessage())
                    .build();
            responseObserver.onNext(CategoryResponseWithException.newBuilder().setExceptionResponse(exceptionResponse).build());
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateCategory(CategoryRequest request, StreamObserver<CategoryResponseWithException> responseObserver) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(request.getId());

            if (optionalCategory.isPresent()) {
                Category existingCategory = optionalCategory.get();
                existingCategory.setName(request.getName());
                existingCategory.setDescription(request.getDescription());
                existingCategory.setActive(request.getActive());

                Category updatedCategory = categoryRepository.save(existingCategory);

                CategoryResponse response = CategoryResponse.newBuilder()
                        .setCategory(toProto(updatedCategory))
                        .build();
                responseObserver.onNext(CategoryResponseWithException.newBuilder().setCategoryResponse(response).build());
            } else {
                throw new IllegalArgumentException("Category with id " + request.getId() + " not found");
            }
        } catch (Exception e) {
            ExceptionResponseOuterClass.ExceptionResponse exceptionResponse = ExceptionResponseOuterClass.ExceptionResponse.newBuilder()
                    .setCode(ErrorCodeOuterClass.ErrorCode.CATEGORY_NOT_FOUND)
                    .setMessage(e.getMessage())
                    .build();
            responseObserver.onNext(CategoryResponseWithException.newBuilder().setExceptionResponse(exceptionResponse).build());
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void deleteCategory(CategoryIdRequest request, StreamObserver<CategoryResponseWithException> responseObserver) {
        try {
            Optional<Category> categoryOptional = categoryRepository.findById(request.getId());

            if (categoryOptional.isPresent()) {
                Category category = categoryOptional.get();
                categoryRepository.delete(category);

                CategoryResponse response = CategoryResponse.newBuilder()
                        .setCategory(toProto(category))
                        .build();
                responseObserver.onNext(CategoryResponseWithException.newBuilder().setCategoryResponse(response).build());
            } else {
                throw new IllegalArgumentException("Category with id " + request.getId() + " not found");
            }
        } catch (Exception e) {
            ExceptionResponseOuterClass.ExceptionResponse exceptionResponse = ExceptionResponseOuterClass.ExceptionResponse.newBuilder()
                    .setCode(ErrorCodeOuterClass.ErrorCode.CATEGORY_NOT_FOUND)
                    .setMessage(e.getMessage())
                    .build();
            responseObserver.onNext(CategoryResponseWithException.newBuilder().setExceptionResponse(exceptionResponse).build());
        } finally {
            responseObserver.onCompleted();
        }
    }

    private com.example.category.Category toProto(Category category) {
        Timestamp createdDate = Timestamp.newBuilder()
                .setSeconds(category.getCreatedDate().atZone(ZoneId.systemDefault()).toEpochSecond())
                .build();

        Timestamp lastUpdatedDate = Timestamp.newBuilder()
                .setSeconds(category.getLastUpdatedDate().atZone(ZoneId.systemDefault()).toEpochSecond())
                .build();

        return com.example.category.Category.newBuilder()
                .setId(category.getId())
                .setName(category.getName())
                .setDescription(category.getDescription())
                .setCreatedDate(createdDate)
                .setLastUpdatedDate(lastUpdatedDate)
                .setActive(category.getActive())
                .build();
    }
}

